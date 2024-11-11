docker build -t pong:latest .
docker tag pong:latest pong:latest
kubectl apply -f .\deployment.yaml