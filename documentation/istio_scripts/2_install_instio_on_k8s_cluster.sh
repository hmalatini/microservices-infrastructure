#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
LIGHT_BLUE='\033[1;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}Kubernetes available contexts are:${NC}"
kubectl config get-contexts
echo ""

echo -e "${YELLOW}You are going to install istio in the following cluster:${NC}"
kubectl config current-context
echo -e "${YELLOW}Do you want to continue?${NC}"
read -p '(y/n): ' cont

case "$cont" in
n) echo -e "To change the context, execute 'kubectl config use-context my-context-name'\n${RED}Exit program${NC}" && exit 1 ;;
n) echo -e "To change the context, execute 'kubectl config use-context my-context-name'\n${RED}Exit program${NC}" && exit 1 ;;
y) echo -e "Continue execution..." && echo "" ;;
Y) echo -e "Continue execution..." && echo "" ;;
?) echo -e "${RED}Didn't understand you.${NC}\nTo change the context, execute 'kubectl config use-context my-context-name'\n${RED}Exit program${NC}" && exit 1 ;;
esac

echo -e "${YELLOW}What profile would you like to install? ${NC}"
echo -e "${GREEN}1: ${LIGHT_BLUE}default${NC} - This profile is recommended for production deployments and for primary clusters in a multicluster mesh."
echo -e "${GREEN}2: ${LIGHT_BLUE}remote${NC} - Used for configuring remote clusters of a multicluster mesh."
echo -e "${GREEN}3: ${LIGHT_BLUE}minimal${NC} - Same as the default profile, but only the control plane components are installed. This allows you to configure the control plane and data plane components (e.g., gateways) using separate profiles."
echo -e "${GREEN}4: ${LIGHT_BLUE}empty${NC} - Deploys nothing. This can be useful as a base profile for custom configuration."
echo -e "${GREEN}5: ${LIGHT_BLUE}demo${NC} - Configuration designed to showcase Istio functionality with modest resource requirements. It is suitable to run the Bookinfo example application and associated tasks."
echo -e "${GREEN}6: ${LIGHT_BLUE}preview${NC} - The preview profile contains features that are experimental. This is intended to explore new features coming to Istio. Stability, security, and performance are not guaranteed - use at your own risk."
echo ""
echo -e "For more information about profiles, visit: https://istio.io/latest/docs/setup/additional-setup/config-profiles/"

read -p "Introduce the number according to the profile: " prof
case "$prof" in
1) selectedProfile="default" ;;
2) selectedProfile="remote" ;;
3) selectedProfile="minimal" ;;
4) selectedProfile="empty" ;;
5) selectedProfile="demo" ;;
6) selectedProfile="preview" ;;
?) echo -e "${RED}Didn't understand you.${NC}\n${RED}Exit program${NC}" && exit 1 ;;
esac

echo -e "${LIGHT_BLUE}Selected Profile: ${selectedProfile}${NC}"

echo -e "${GREEN}Installing Istio on Kubernetes Cluster"
istioctl install --set profile=${selectedProfile}

echo -e "${GREEN}Checking initialization on Kubernetes:${NC}"
kubectl get deployments,services -n istio-system

echo -e "${GREEN}Checking istio version of control and data plane:${NC}"
istioctl version