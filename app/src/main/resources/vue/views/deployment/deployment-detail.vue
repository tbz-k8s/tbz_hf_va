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
                        <b-col md="4">
                            <b-card-body>
                                <b-card-title class="deployment-card-title">Replaced Envs</b-card-title>
                                <b-card-text class="replaced-env-text" v-for="env in deployment.replacedEnvs">
                                    <span>{{env.name}}: {{env.value}}</span>
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                        <b-col md="2">
                            <b-card-body>
                                <b-card-title class="deployment-card-title">External Access</b-card-title>
                                <b-card-text>
                                    <a v-for="externalAccess in deployment.externalAccess"
                                          :href="'http://' + getLink(externalAccess)">
                                        {{ getLink(externalAccess)}}
                                    </a>
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                        <b-col md="2">
                            <b-card-body>
                                <b-card-title class="deployment-card-title">Cluster Access</b-card-title>
                                <b-card-text>
                                    <a v-for="clusterAccess in deployment.clusterAccess"
                                       :href="'http://' + getLink(clusterAccess)">
                                        {{ getLink(clusterAccess)}}
                                    </a>
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                        <b-col md="2" v-if="deployment.trainee">
                            <b-card-body>
                                <b-card-title class="deployment-card-title">Trainee</b-card-title>
                                <b-card-text class="trainee-text">
                                    <div>Name: {{ deployment.trainee.name }}</div>
                                    <div>Email: {{ deployment.trainee.email }}</div>
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                        <b-col md="1" v-if="deployment.trainee">
                            <b-card-body>
                                <b-button variant="outline-primary"
                                          @click="sendSingleMail(deployment.deploymentNumber)">
                                    Mail
                                </b-button>
                            </b-card-body>
                        </b-col>
                    </b-row>
                </b-card>
            </div>
            <b-row>
                <b-col>
                    <nav-back return_to="/deployments"/>
                </b-col>
                <b-col>
                    <div class="float-right">
                        <b-button variant="outline-primary" @click="sendAllMail()">Send mails for all Trainees
                        </b-button>
                    </div>
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
                    clusterAccess: [],
                    trainee: {
                        name: "",
                        email: ""
                    },
                    state: {ready: '', total: ''},
                    deploymentNumber: null
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
            },
            getLink(access) {
                return `${access.ip}:${access.ports[0]}`
            },
            sendSingleMail(deploymentNumber) {
                axios.post(`/api/v1/deployment/${this.deploymentName}/mail/${deploymentNumber}`)
                    .catch((error) => this.showErrorMessage(error.response.statusText));
            },
            sendAllMail() {
                axios.post(`/api/v1/deployment/${this.deploymentName}/mail`)
                    .catch((error) => this.showErrorMessage(error.response.statusText));
            },
            showErrorMessage(error) {
                this.$bvToast.toast("" + error, {
                    title: 'Error',
                    autoHideDelay: 5000,
                    variant: 'danger'
                })
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
        font-size: 1.1rem;
    }

    .replaced-env-text {
        margin-bottom: 2px;
        font-size: 0.9rem;
    }

    .container {
        max-width: 2500px;
    }

    .trainee-text {
        font-size: 0.8rem;
    }

</style>