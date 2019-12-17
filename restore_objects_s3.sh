aws s3api list-object-versions --bucket "$BUCKET_NAME" --prefix "$PREFIX" --output text --region eu-west-1 | grep "DELETEMARKERS" >delete_markers.out
cat delete_markers.out
while read -r obj; do
  KEY=$(echo "$obj" | awk '{print $3}')
  VERSION_ID=$(echo "$obj" | awk '{print $5}')
  echo "$KEY"
  echo "$VERSION_ID"
  aws s3api delete-object --bucket "$BUCKET_NAME" --key "$KEY" --version-id "$VERSION_ID" --region eu-west-1
done <delete_markers.out
