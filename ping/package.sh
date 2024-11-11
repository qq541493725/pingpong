docker build -t ping:latest .
docker tag ping:latest ping:latest
kubectl apply -f .\deployment.yaml