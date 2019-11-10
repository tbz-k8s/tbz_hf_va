<template id="deployment-detail">
    <div>
        <navigation active_navigation="Deployment Detail"></navigation>
        <b-container>
            <h1>{{deploymentName}}</h1>
            <b-progress :max="data.totalDeployments" show-value class="deployment-progress">
                <b-progress-bar :value="data.totalReady" variant="success"></b-progress-bar>
                <b-progress-bar :value="data.totalDeployments - data.totalReady" variant="warning"></b-progress-bar>
            </b-progress>
            <div v-for="deployment in data.deployments">
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
            <b-row>
                <b-col>
                    <nav-back return_to="/deployments"/>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>

<script>
    Vue.component("deployment-detail", {
        template: "#deployment-detail",
        data: () => ({
            deploymentName: "",
            data: {
                deployments: [{
                    replacedEnvs: [{name: '', value: ''}],
                    externalAccess: [],
                    state: {ready: '', total: ''}
                }],
                totalReady: 0,
                totalDeployments: 0
            }

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
                        this.data = res;
                        console.log(res)
                    })
                    .catch(() => "")
            }
        }
    });
</script>

<style scoped>
    .deployment-card, .deployment-progress {
        margin-bottom: 10px;
    }

    .deployment-progress {
        height: 40px;
    }

    .deployment-card-title {
        font-size: 1.3rem;
    }

</style>