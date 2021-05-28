<template>
  <div>
    <div id="title" v-if="$route.params.microserviceName">
      {{ $route.params.microserviceName.toUpperCase() }}
    </div>
    <div v-if="microservice" style="margin-top: 40px;"
         class="d-flex align-items-start justify-content-center flex-wrap">
      <div id="get-started">
        <div style="margin-top: 30px;"></div>
        <p>TO GET STARTED:</p>
        <div id="steps">
          <p>$ git clone {{ microservice.gitUrl }}.git</p>
          <p>$ cd {{ microservice.name }}</p>
        </div>
      </div>
    </div>
    <div v-if="microservice" style="margin-top: 40px;"
         class="d-flex align-items-start justify-content-center flex-wrap">
      <a v-bind:href="microservice.gitUrl">
        <mdb-btn style="background-color: #0f0f0f !important;">GitHub</mdb-btn>
      </a>
    </div>
  </div>
</template>

<script>
import backendHttpClient from "@/plugins/axios";

export default {
  name: "MicroserviceInfo",
  data() {
    return {
      microservice: null,
    };
  },
  created: function () {
    this.getMicroservice()
  },
  methods: {
    getMicroservice() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get('microservice/' + serviceName).then(microserviceResponse => {
        this.microservice = microserviceResponse.data
      })
    },
  }
}
</script>

<style scoped>
#title {
  margin-top: 15px;
  font-weight: bold;
  font-size: 2.5rem;
}

#get-started {
  height: 200px;
  width: 600px;
  background: #0f0f0f 0 0 no-repeat padding-box;
  box-shadow: 0 3px 6px #00000029;
  border-radius: 10px;
  opacity: 1;
  color: white;
}

#steps {
  text-align: left;
  margin-left: 90px;
  padding-left: 30px;
  width: 420px;
  background: #1b1e21 0 0 no-repeat padding-box;
  box-shadow: 0 3px 6px #00000029;
  border-radius: 10px;
  opacity: 1;
}
</style>