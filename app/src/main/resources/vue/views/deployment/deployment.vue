<template id="deployment">
    <div>
        <navigation :active_navigation="'Deployment'"></navigation>
        <b-container>
            <b-form @submit="addDeployment">
                <b-form-group id="deployment-name-group"
                              label="Name of deployment"
                              label-for="deployment-name"
                              description="Set the name of the Deployment. Has to be unique in the Cluster">
                    <b-form-input id="deployment-name"
                                  v-model="deployment.name"
                                  type="text"
                                  required
                                  placeholder="Deployment Name"></b-form-input>
                </b-form-group>
                <b-form-group id="deployment-replication-group"
                              label="Number of deployments"
                              label-for="deployment-replication"
                              description="Set the number of deployments in the cluster">
                    <b-form-input id="deployment-replication"
                                  v-model="deployment.replication"
                                  type="number"
                                  required
                                  placeholder="0"></b-form-input>
                </b-form-group>
                <b-form-group id="repo-select-group"
                              label="Repo to deploy"
                              label-for="repo-select"
                              description="The Repo containing the template to deploy">
                    <b-form-select id="repo-select"
                                   v-model="repo"
                                   :options="repos">

                    </b-form-select>
                </b-form-group>
                <b-row>
                    <b-col>
                        <nav-back return_to="/deployments"/>
                    </b-col>
                    <b-col>
                        <div class="float-right">
                            <b-button type="submit" variant="primary">Submit</b-button>
                            <b-button type="reset" variant="outline-danger">Reset</b-button>
                        </div>
                    </b-col>
                </b-row>
            </b-form>
        </b-container>
    </div>
</template>

<script>
    function clearInput() {
        this.repo = {name: "", url: "", id: ""}
    }

    Vue.component("deployment", {
        template: "#deployment",
        data: () => ({
            deployment: {
                name: "",
                replication: 0,
                shouldPersist: false,
                shouldDeleteAfterShutdown: false
            },
            repo: {},
            repos: [{text: 'Select One', value: null}]
        }),
        created() {
            fetch("/api/v1/repos")
                .then(res => res.json())
                .then(res => {
                    this.repos = res.map(repo => {
                        return {text: repo.name, value: repo}
                    })
                })
                .catch(() => "")
        },
        methods: {
            addDeployment(evt) {
                evt.preventDefault();
                axios.post("/api/v1/deployment", {
                    deployment: this.deployment,
                    repositoryId: this.repo.id
                }).then(() => {
                    window.location.href = `/deployment/${this.deployment.name.toLowerCase()}`;
                }).catch((error) => {
                    this.$bvToast.toast("" + error.response.statusText, {
                        title: 'Error Saving Record',
                        autoHideDelay: 5000,
                        variant: 'danger'
                    })
                })

            },
            onReset(evt) {
                clearInput()
            }
        }
    })
    ;
</script>

<style scoped>

</style>