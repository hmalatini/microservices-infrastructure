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
    echo "Usage: $0 -v 1.8.2 -d"
    echo -e "\t-v Version of the Istio. If not provided, default version will be used"
    echo -e "\t-d If you want to download the version, use this flag"
}


# Parse script parameters from command line
while getopts "v:d" opt; do
    case "$opt" in
    v) version="$OPTARG" ;;
    d) download="true" ;;
    ?) helpFunctionAndExit ;; # Print helpFunction in case parameter is non-existent
    esac
done

if [ -z "$version" ]; then
    version="1.9.0"
fi


echo -e "${LIGHT_BLUE}Exporting ISTIO_VERSION:${YELLOW} ${version} ${NC}"
export ISTIO_VERSION=${version}
echo ""

if [ -n "$download" ]; then
	cd ${DIR}
	echo -e "${YELLOW}Downloading Istio in ${DIR} ${NC}"
    curl -L https://istio.io/downloadIstio | sh -
    echo ""
fi

echo -e "${LIGHT_BLUE}Exporting dir to PATH${NC}"
export PATH="$PATH:${DIR}/istio-${ISTIO_VERSION}/bin"
echo ""

echo -e "${GREEN}Running istioctl version command:${NC}"
istioctl version
echo ""

echo -e "${RED}RUN THE FOLLOWING COMMAND IN YOUR TERMINAL:${NC}"
echo export PATH="\$PATH:${DIR}/istio-${ISTIO_VERSION}/bin"