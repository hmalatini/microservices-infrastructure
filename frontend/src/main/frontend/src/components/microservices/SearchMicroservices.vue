<template>
  <div>
    <div id="create">
      <router-link to="/microservices/create">
        <mdb-btn style="background-color: #00A082;">Create New Microservice</mdb-btn>
      </router-link>
    </div>
    <div id="table">
      <mdb-input class="mt-0" v-model="search" label="Search by name"/>
      <mdb-datatable-2 hover v-model="data" @selected="redirect($event)" :searching="{value: search, field: 'name'}"/>
    </div>
  </div>
</template>

<script>

import backendHttpClient from "@/plugins/axios";
import router from "@/router";

function getDataWithMicroservicesNames(microserviceNameList) {
  return {
    columns: [
      {
        label: 'Name',
        field: 'name',
        sort: 'asc'
      },
    ],
    rows: microserviceNameList
  };
}

function getMicroservicesNameList(microserviceResponse) {
  let microserviceNameList = []

  microserviceResponse.data.forEach(microservice => {
    microserviceNameList.push({
      name: microservice.name
    })
  })

  return microserviceNameList;
}
export default {
  name: "SearchMicroservices",
  data() {
    return {
      selected: null,
      search: '',
      data: getDataWithMicroservicesNames([])
    }
  },
  methods: {
    redirect: function (event) {
      const microserviceName = event.name
      router.push({path: `/microservices/${microserviceName}/info` })
    }
  },
  created: function () {
    backendHttpClient.get('microservice/all').then(microserviceResponse => {
      let microserviceNameList = getMicroservicesNameList(microserviceResponse)
      this.data = getDataWithMicroservicesNames(microserviceNameList)
    })
  }
}
</script>

<style scoped>
#create {
  padding: 30px 0;
}

#table {
  padding: 0 40%;
}
</style>