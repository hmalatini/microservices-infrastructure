import Vue from 'vue'
import VueRouter from 'vue-router'

import SearchMicroservices
  from "@/components/microservices/SearchMicroservices";
import NotFound from "@/components/NotFound";
import CreateMicroservice from "@/components/microservices/CreateMicroservice";
import MicroserviceView from "@/components/microservices/MicroserviceView";
import MicroserviceInfo from "@/components/microservices/MicroserviceInfo";
import EnvironmentInfo
  from "@/components/microservices/environment/EnvironmentInfo";
import SecretsInfo from "@/components/microservices/secrets/SecretsInfo";
import ReleaseInfo from "@/components/microservices/releases/ReleaseInfo";
import CreateDeployment
  from "@/components/microservices/deployment/CreateDeployment";
import ConfigInfo from "@/components/microservices/config/ConfigInfo";
import CreateEnvironment
  from "@/components/microservices/environment/CreateEnvironment";
import CreateSecret from "@/components/microservices/secrets/CreateSecret";
import CreateRelease from "@/components/microservices/releases/CreateRelease";
import CronJobInfo from "@/components/microservices/cronjob/CronJobInfo";
import CreateCronJob from "@/components/microservices/cronjob/CreateCronJob";
import TrafficPolicyInfo
  from "@/components/microservices/trafficpolicy/TrafficPolicyInfo";
import EndpointInfo from "@/components/microservices/endpoint/EndpointInfo";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/microservices'
  },
  {
    path: '',
    redirect: '/microservices'
  },
  {
    path: '/microservices',
    name: 'SearchMicroservices',
    component: SearchMicroservices
  },
  {
    path: '/microservices/create',
    name: 'CreateMicroservice',
    component: CreateMicroservice
  },
  {
    path: '/microservices/:microserviceName',
    component: MicroserviceView,
    children: [
      {
        path: '',
        redirect: 'info'
      },
      {
        path: 'info',
        name: 'MicroserviceInfo',
        component: MicroserviceInfo,
        props: {page: 1},
        alias: '/'
      },
      {
        path: 'environment',
        name: 'EnvironmentInfo',
        component: EnvironmentInfo,
        props: {page: 2},
      },
      {
        path: 'environment/create',
        name: 'CreateEnvironment',
        component: CreateEnvironment,
        props: {page: 2},
      },
      {
        path: 'secrets',
        name: 'SecretsInfo',
        component: SecretsInfo,
        props: {page: 3},
      },
      {
        path: 'secrets/create',
        name: 'CreateSecret',
        component: CreateSecret,
        props: {page: 3},
      },
      {
        path: 'cronjob',
        name: 'CronJobInfo',
        component: CronJobInfo,
        props: {page: 4},
      },
      {
        path: 'cronjob/create',
        name: 'CreateCronJob',
        component: CreateCronJob,
        props: {page: 4},
      },
      {
        path: 'releases',
        name: 'ReleaseInfo',
        component: ReleaseInfo,
        props: {page: 5},
      },
      {
        path: 'releases/create',
        name: 'CreateRelease',
        component: CreateRelease,
        props: {page: 5},
      },
      {
        path: 'deployment',
        name: 'CreateDeployment',
        component: CreateDeployment,
        props: {page: 6},
      },
      {
        path: 'traffic-policy',
        name: 'TrafficPolicyInfo',
        component: TrafficPolicyInfo,
        props: {page: 7},
      },
      {
        path: 'endpoint',
        name: 'EndpointInfo',
        component: EndpointInfo,
        props: {page: 8},
      },
      {
        path: 'config',
        name: 'ConfigInfo',
        component: ConfigInfo,
        props: {page: 9},
      },
    ]
  },
  {
    path: '*',
    name: 'NotFound',
    component: NotFound
  },
  //{
  //  path: '/about',
  //  name: 'About',
  // route level code-splitting
  // this generates a separate chunk (about.[hash].js) for this route
  // which is lazy-loaded when the route is visited.
  //  component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  //}
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
