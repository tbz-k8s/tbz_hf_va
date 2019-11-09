<template id="deployment-detail">
    <div>
        <navigation active_navigation="Deployment Detail"></navigation>
        <b-container>
            <h1>{{deploymentName}}</h1>
            <div v-for="deployment in deployments">
                <b-card no-body class="deployment-card" title="Bla" sub-title="Card subtitle"
                        style="min-width: 800px;">
                    <b-row no-gutters>
                        <b-col md="6">
                            <b-card-body title="Replaced Envs">
                                <b-card-text>
                                    <span v-for="env in deployment.replacedEnvs">{{env.name}}: {{env.value}}</span>
                                </b-card-text>
                            </b-card-body>
                        </b-col>
                        <b-col md="4">
                            <b-card-body title="State">
                                <b-card-text>
                                    This is a wider card with supporting text as a natural lead-in to additional
                                    content.
                                    This content is a little bit longer.
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
                externalAccess: []
            }]
        }),
        created() {
            const deploymentName = this.$javalin.pathParams["deployment-name"];
            this.deploymentName = deploymentName;
            fetch(`/api/v1/deployment/${deploymentName}`)
                .then(res => res.json())
                .then(res => {
                    this.deployments = res
                    console.log(res)
                })
                .catch(() => "")
        },
        methods: {}
    });
</script>

<style scoped>
    .deployment-card {
        margin-bottom: 10px;
    }

    h1 {

    }
</style>