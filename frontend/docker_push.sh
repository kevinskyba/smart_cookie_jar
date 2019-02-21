tag=`cat Dockertag`
version=$(cat package.json \
           | grep version \
           | head -1 \
           | awk -F: '{ print $2 }' \
           | sed 's/[",]//g' \
           | tr -d '[[:space:]]')

docker push $tag:latest
docker tag $tag:latest $tag:$version
docker push $tag:$version