#!/bin/bash

# ===> Set these variables first
branch="master"
repo_slug="$TRAVIS_REPO_SLUG"
token="$GH_CHANGELOG_RELEASE_KEY"
version="$TRAVIS_TAG"
release_id=$(curl -H "Authorization: token $token" "https://api.github.com/repos/$repo_slug/releases/latest" | jq '.id')

echo "Starting to edit release $release_id..."

# An automatic changelog generator
gem install github_changelog_generator

LAST_REVISION=$(git rev-list --tags --skip=1 --max-count=1)
LAST_RELEASE_TAG=$(git describe --abbrev=0 --tags ${LAST_REVISION})

# Generate CHANGELOG.md
github_changelog_generator \
  -u $(cut -d "/" -f1 <<< $repo_slug) \
  -p $(cut -d "/" -f2 <<< $repo_slug) \
  --token $token \
  --no-issues \
  --since-tag ${LAST_RELEASE_TAG}

body="$(cat CHANGELOG.md)"

# Overwrite CHANGELOG.md with JSON data for GitHub API
jq -n \
  --arg body "$body" \
  --arg name "$version" \
  --arg tag_name "$version" \
  --arg target_commitish "$branch" \
  '{
    body: $body,
    name: $name,
    tag_name: $tag_name,
    target_commitish: $target_commitish,
    draft: false,
    prerelease: false
  }' > CHANGELOG.md

echo "Edit release $version for repo: $repo_slug, branch: $branch"
curl -H "Authorization: token $token" --data @CHANGELOG.md "https://api.github.com/repos/$repo_slug/releases/$release_id"
echo "Done"
