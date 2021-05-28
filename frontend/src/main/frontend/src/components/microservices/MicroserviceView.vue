<template>
  <div>
    <!-- Sidebar -->
    <div class="sidebar-fixed position-fixed">
      <mdb-list-group class="list-group-flush">
        <router-link to="info" @click.native="activeItem = 1">
          <mdb-list-group-item :action="true" :class="activeItem === 1 && 'active'">
            <mdb-icon icon="info-circle" class="mr-3"/>
            Info
          </mdb-list-group-item>
        </router-link>
        <router-link to="environment" @click.native="activeItem = 2">
          <mdb-list-group-item :action="true" :class="activeItem === 2 && 'active'">
            <mdb-icon icon="server" class="mr-3"/>
            Environments
          </mdb-list-group-item>
        </router-link>
        <router-link to="secrets" @click.native="activeItem = 3">
          <mdb-list-group-item :action="true" :class="activeItem === 3 && 'active'">
            <mdb-icon icon="shield-alt" class="mr-3"/>
            Secrets
          </mdb-list-group-item>
        </router-link>
        <router-link to="cronjob" @click.native="activeItem = 4">
          <mdb-list-group-item :action="true" :class="activeItem === 4 && 'active'">
            <mdb-icon icon="clock" class="mr-3"/>
            CronJobs
          </mdb-list-group-item>
        </router-link>
        <router-link to="releases" @click.native="activeItem = 5">
          <mdb-list-group-item :action="true" :class="activeItem === 5 && 'active'">
            <mdb-icon icon="code" class="mr-3"/>
            Releases
          </mdb-list-group-item>
        </router-link>
        <router-link to="deployment" @click.native="activeItem = 6">
          <mdb-list-group-item :action="true" :class="activeItem === 6 && 'active'">
            <mdb-icon icon="laptop-code" class="mr-3"/>
            Deployment
          </mdb-list-group-item>
        </router-link>
        <router-link to="traffic-policy" @click.native="activeItem = 7">
          <mdb-list-group-item :action="true" :class="activeItem === 7 && 'active'">
            <mdb-icon icon="traffic-light" class="mr-3"/>
            Traffic Policies
          </mdb-list-group-item>
        </router-link>
        <router-link to="endpoint" @click.native="activeItem = 8">
          <mdb-list-group-item :action="true" :class="activeItem === 8 && 'active'">
            <mdb-icon icon="map-signs" class="mr-3"/>
            Endpoints
          </mdb-list-group-item>
        </router-link>
        <router-link to="config" @click.native="activeItem = 9">
          <mdb-list-group-item :action="true" :class="activeItem === 9 && 'active'">
            <mdb-icon icon="wrench" class="mr-3"/>
            Configuration
          </mdb-list-group-item>
        </router-link>
      </mdb-list-group>
    </div>
    <!-- /Sidebar  -->
    <div id="pageView">
      <router-view/>
    </div>
  </div>
</template>

<script>
export default {
  name: "MicroserviceView",
  data() {
    return {
      activeItem: 1
    };
  },
  beforeMount() {
    if (this.$route.matched[1].props.default !== undefined) {
      this.activeItem = this.$route.matched[1].props.default.page;
    }
  }
}
</script>

<style scoped>
#pageView {
  margin-left: 271px;
}

.sidebar-fixed {
  left: 0;
  top: 56px;
  height: calc(100vh - 56px);
  width: 270px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
  z-index: 1050;
  background-color: #fff;
  padding: 1.5rem 0;
  padding-top: 0;
}

.sidebar-fixed .list-group-item {
  display: block !important;
  transition: background-color 0.3s;
}

.sidebar-fixed .list-group .active {
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 160, 130, 0.12);
  border-radius: 5px;
  background-color: #00A082;
}

.sidebar-fixed .list-group-item-action {
  text-align: left;
}

@media (max-width: 1199.98px) {
  #pageView {
    margin-left: 0px;
  }

  .sidebar-fixed {
    display: none;
  }
}
</style>