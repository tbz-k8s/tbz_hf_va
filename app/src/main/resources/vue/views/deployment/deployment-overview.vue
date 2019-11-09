<template id="deployments">
    <div>
        <navigation active_navigation="Deployments"></navigation>
        <b-container>
            <h1>Existing Deployments</h1>
            <b-table id="deployment-table" hover :items="deployments" :fields="rows">
                <template v-slot:cell(action)="data">
                    <div class="float-right" v-if="data.item.status !== 'Terminating'">
                        <b-button :href="'/deployment/' + data.item.name" variant="outline-primary">
                            Edit {{ data.item.name }}
                        </b-button>
                        <b-button @click="deleteDeployment(data.item.name)" variant="outline-danger">
                            X
                        </b-button>
                    </div>
                </template>
            </b-table>
            <b-row>
                <b-col>
                    <b-button href="/deployment" variant="success" class="float-right">Add Deployment</b-button>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>

<script>
    Vue.component("deployments", {
        template: "#deployments",
        data: () => ({
            deployments: [],
            rows: [
                {
                    key: 'name',
                    sortable: true
                },
                {
                    key: 'replications',
                    sortable: true
                },
                {
                    key: 'status',
                    sortable: false
                },
                {
                    key: 'action',
                    sortable: false,
                    label: ''
                }
            ]
        }),
        created() {
            this.loadData();
            setInterval(function () {
                this.loadData();
            }.bind(this), 2000);
        },
        methods: {
            deleteDeployment(name) {
                axios.delete(`/api/v1/deployment/${name}`)
            },
            loadData() {
                fetch("/api/v1/deployments")
                    .then(res => res.json())
                    .then(res => {
                        this.deployments = res
                    })
                    .catch(() => "")
            }
        }
    });
</script>

<style scoped>

</style>