#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
LIGHT_BLUE='\033[1;34m'
NC='\033[0m' # No Color

helpFunctionAndExit() {
    helpFunction
    exit 1
}

helpFunction() {
    echo ""
    echo "Usage: $0 -v 1.8.2"
    echo -e "\t-v Version of the Istio. If not provided, default version will be used"
}

#1 Extension name
#2 Link to yaml file
installFunction() {
  echo -e "Would you like to install ${LIGHT_BLUE}$1${NC}"
  read -p '(y/n): ' install

  if [ ${install} = 'y' ] || [ ${install} = 'Y' ]; then
      echo -e "${GREEN}Installing $1:${NC}"
      if [ $1 = 'Kiali' ]; then
          kubectl create -f kiali-crd-fix.yaml -n istio-system
      fi
      kubectl apply -f $2 -n istio-system
  fi
  echo ""
}


# Parse script parameters from command line
while getopts "v:d" opt; do
    case "$opt" in
    v) version="$OPTARG" ;;
    ?) helpFunctionAndExit ;; # Print helpFunction in case parameter is non-existent
    esac
done

if [ -z "$version" ]; then
    version="1.9.0"
fi

echo -e "${LIGHT_BLUE}Istio current version on system:${NC}"
istioctl version
echo -e "${YELLOW}You are going to install integrations for Istio version --> ${version} <--. Do you want to continue?${NC}"
read -p '(y/n): ' cont

case "$cont" in
n) echo -e "To know how to change integrations version execute 'istio_integrations.sh --help'\n${RED}Exit program${NC}" && exit 1 ;;
N) echo -e "To know how to change integrations version execute 'istio_integrations.sh --help'\n${RED}Exit program${NC}" && exit 1 ;;
y) echo -e "Continue execution..." && echo "" ;;
Y) echo -e "Continue execution..." && echo "" ;;
?) echo -e "${RED}Didn't understand you.${NC}\nTo know how to change integrations version execute 'istio_integrations.sh --help'\n${RED}Exit program${NC}" && exit 1 ;;
esac

export ISTIO_VERSION=${version}
echo -e "${YELLOW}Integrations version:"
SEMVER_REGEX='[^0-9]*\([0-9]*\)[.]\([0-9]*\)[.]\([0-9]*\)\([0-9A-Za-z-]*\)'
INTEGRATIONS_VERSION=$(echo $ISTIO_VERSION | sed -e "s#$SEMVER_REGEX#\1#").$(echo $ISTIO_VERSION | sed -e "s#$SEMVER_REGEX#\2#") && echo $INTEGRATIONS_VERSION
echo -e "${NC}"

installFunction "Prometheus" "https://raw.githubusercontent.com/istio/istio/release-$INTEGRATIONS_VERSION/samples/addons/prometheus.yaml"
installFunction "Grafana" "https://raw.githubusercontent.com/istio/istio/release-$INTEGRATIONS_VERSION/samples/addons/grafana.yaml"
installFunction "Jaeger" "https://raw.githubusercontent.com/istio/istio/release-$INTEGRATIONS_VERSION/samples/addons/jaeger.yaml"
installFunction "Kiali" "https://raw.githubusercontent.com/istio/istio/release-$INTEGRATIONS_VERSION/samples/addons/kiali.yaml"
