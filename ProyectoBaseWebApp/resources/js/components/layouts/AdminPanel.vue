<template>
  <div class="sidebar-page">
    <section class="sidebar-layout">
      <b-sidebar
        type="is-light"
        :open.sync="open"
        fullheight
        expand-on-hover
        :can-cancel="['x', 'outside', 'escape']"
      >
        <div class="p-1">
          <img src="/img/logo.png" alt="Logo" />
          <b-menu class="is-custom-mobile">
            <b-menu-list :label="$t('title.menu')">
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== ''"
                to="/admin"
                tag="router-link"
                icon="monitor"
                :label="$t('title.dashboard')"
              ></b-menu-item>
              <b-menu-item
                icon="office-building"
                :label="$t('title.empresas')"
                tag="router-link"
                to="/admin/empresas"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_roles') ||
                $store.getters.permiteAccion('editar_roles') || $store.getters.permiteAccion('consultar_roles') ||
                $store.getters.permiteAccion('eliminar_roles'))"
                icon="account-lock"
                :label="$t('title.roles')"
                tag="router-link"
                to="/admin/roles"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_usuarios') ||
                $store.getters.permiteAccion('editar_usuarios') || $store.getters.permiteAccion('consultar_usuarios') ||
                $store.getters.permiteAccion('eliminar_usuarios'))"
                icon="account-multiple"
                :label="$t('title.usuarios')"
                tag="router-link"
                to="/admin/usuarios"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_largos') ||
                $store.getters.permiteAccion('editar_largos') || $store.getters.permiteAccion('consultar_largos') ||
                $store.getters.permiteAccion('eliminar_largos'))"
                icon="ruler"
                :label="$t('title.largos')"
                tag="router-link"
                to="/admin/largos"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_espesores') ||
                $store.getters.permiteAccion('editar_espesores') || $store.getters.permiteAccion('consultar_espesores') ||
                $store.getters.permiteAccion('eliminar_espesores'))"
                icon="weight"
                :label="$t('title.espesores')"
                tag="router-link"
                to="/admin/espesores"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_procedencias') ||
                $store.getters.permiteAccion('editar_procedencias') || $store.getters.permiteAccion('consultar_procedencias') ||
                $store.getters.permiteAccion('eliminar_procedencias'))"
                icon="source-branch"
                :label="$t('title.procedencias')"
                tag="router-link"
                to="/admin/procedencias"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_destinos') ||
                $store.getters.permiteAccion('editar_destinos') || $store.getters.permiteAccion('consultar_destinos') ||
                $store.getters.permiteAccion('eliminar_destinos'))"
                icon="map-marker"
                :label="$t('title.destinos')"
                tag="router-link"
                to="/admin/destinos"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_materiales') ||
                $store.getters.permiteAccion('editar_materiales') || $store.getters.permiteAccion('consultar_materiales') ||
                $store.getters.permiteAccion('eliminar_materiales'))"
                icon="material-ui"
                :label="$t('title.materiales')"
                tag="router-link"
                to="/admin/materiales"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_correos') ||
                $store.getters.permiteAccion('editar_correos') || $store.getters.permiteAccion('consultar_correos') ||
                $store.getters.permiteAccion('eliminar_correos'))"
                icon="email"
                :label="$t('title.correos')"
                tag="router-link"
                to="/admin/correos"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_controladores') ||
                $store.getters.permiteAccion('editar_controladores') || $store.getters.permiteAccion('consultar_controladores') ||
                $store.getters.permiteAccion('eliminar_controladores'))"
                icon="nature-people"
                :label="$t('title.controladores')"
                tag="router-link"
                to="/admin/controladores"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_aserradores') ||
                $store.getters.permiteAccion('editar_aserradores') || $store.getters.permiteAccion('consultar_aserradores') ||
                $store.getters.permiteAccion('eliminar_aserradores'))"
                icon="account-multiple-outline"
                :label="$t('title.aserradores')"
                tag="router-link"
                to="/admin/aserradores"
              ></b-menu-item>
              <b-menu-item
                v-show="$store.state.nombre_empresa_actual !== '' && ($store.getters.permiteAccion('crear_camiones') ||
                $store.getters.permiteAccion('editar_camiones') || $store.getters.permiteAccion('consultar_camiones') ||
                $store.getters.permiteAccion('eliminar_camiones'))"
                icon="dump-truck"
                :label="$t('title.camiones')"
                tag="router-link"
                to="/admin/camiones"
              ></b-menu-item>
            </b-menu-list>
          </b-menu>
        </div>
      </b-sidebar>
      <div class="p-1 sidebar-body">
        <b-button @click="open = !open" icon-left="menu">
          {{
          $t("title.menu")
          }}
        </b-button>
        <router-view></router-view>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  data: function () {
    return {
      open: false,
    };
  },
};
</script>

<style lang="scss">
.p-1 {
  padding: 1em;
}
.sidebar-body {
  width: 100%;
}
.sidebar-page {
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100%;
  // min-height: 100vh;
  .sidebar-layout {
    display: flex;
    flex-direction: row;
    min-height: 100%;
    // min-height: 100vh;
  }
}
@media screen and (max-width: 1023px) {
  .b-sidebar {
    .sidebar-content {
      &.is-mini-mobile {
        &:not(.is-mini-expand),
        &.is-mini-expand:not(:hover) {
          .menu-list {
            li {
              a {
                span:nth-child(2) {
                  display: none;
                }
              }
              ul {
                padding-left: 0;
                li {
                  a {
                    display: inline-block;
                  }
                }
              }
            }
          }
          .menu-label:not(:last-child) {
            margin-bottom: 0;
          }
        }
      }
    }
  }
}
@media screen and (min-width: 1024px) {
  .b-sidebar {
    .sidebar-content {
      &.is-mini {
        &:not(.is-mini-expand),
        &.is-mini-expand:not(:hover) {
          .menu-list {
            li {
              a {
                span:nth-child(2) {
                  display: none;
                }
              }
              ul {
                padding-left: 0;
                li {
                  a {
                    display: inline-block;
                  }
                }
              }
            }
          }
          .menu-label:not(:last-child) {
            margin-bottom: 0;
          }
        }
      }
    }
  }
}
</style>
