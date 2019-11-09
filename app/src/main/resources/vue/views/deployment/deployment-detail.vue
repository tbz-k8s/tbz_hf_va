<template id="deployment-detail">
    <div>
        <navigation active_navigation="Deployment Detail"></navigation>
        <b-container>
            <h1>{{deploymentName}}</h1>
            <div v-for="deployment in deployments">
                <b-card no-body class="deployment-card" title="Bla" sub-title="Card subtitle"
                        style="min-width: 800px;">
                    <b-row no-gutters>
                        <b-col md="1">
                            <b-card-body>
                                <b-card-title class="deployment-card-title">State</b-card-title>
                                <b-card-text>
                                    {{deployment.state.ready}} / {{deployment.state.total}}
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                        <b-col md="6">
                            <b-card-body title="Replaced Envs">
                                <b-card-text>
                                    <span v-for="env in deployment.replacedEnvs">{{env.name}}: {{env.value}}</span>
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                    </b-row>
                </b-card>
            </div>
        </b-container>
    </div>
</template>

<script>
    Vue.component("deployment-detail", {
        template: "#deployment-detail",
        data: () => ({
            deploymentName: "",
            deployments: [{
                replacedEnvs: [{name: '', value: ''}],
                externalAccess: [],
                state: {ready: '', total: ''}
            }]
        }),
        created() {
            this.deploymentName = this.$javalin.pathParams["deployment-name"];
            this.loadData();
            setInterval(function () {
                this.loadData();
            }.bind(this), 2000);
        },
        methods: {
            loadData() {
                fetch(`/api/v1/deployment/${this.deploymentName}`)
                    .then(res => res.json())
                    .then(res => {
                        this.deployments = res
                        console.log(res)
                    })
                    .catch(() => "")
            }
        }
    });
</script>

<style scoped>
    .deployment-card {
        margin-bottom: 10px;
    }

    .deployment-card-title {
        font-size: 1.3rem;
    }

</style>