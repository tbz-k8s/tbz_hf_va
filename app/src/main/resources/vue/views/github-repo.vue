<template id="github-repo">
    <div>
        <navigation :active_navigation="'Repo: ' + repo.name"></navigation>
        <b-container>
            <b-form @submit="addRepo" @reset="onReset">
                <b-form-group
                        id="repo-name-group"
                        label="Name of Repository:"
                        label-for="repo-name"
                        description="Can be left empty. In this case the name gets parsed from Github Readme">
                    <b-form-input
                            id="repo-name"
                            v-model="repo.name"
                            type="text"
                            placeholder="Enter repo name"></b-form-input>
                </b-form-group>
                <b-form-group
                        id="repo-url-group"
                        label="Url of Repository:"
                        label-for="repo-url"
                        description="Repo">
                    <b-form-input
                            id="repo-url"
                            v-model="repo.url"
                            type="text"
                            required
                            placeholder="Enter repo url, ex: https://github.com/mc-b/duk/tree/master/osticket"></b-form-input>
                </b-form-group>
                <b-row>
                    <b-col>
                        <b-button variant="outline-primary" href="/settings/repos"><-</b-button>
                    </b-col>
                    <b-col>
                        <div class="float-right">
                            <b-button type="submit" variant="primary">Submit</b-button>
                            <b-button type="reset" variant="danger-outline">Reset</b-button>
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

    Vue.component("github-repo", {
        template: "#github-repo",
        data: () => ({
            repo: {name: "", url: "", id: ""}
        }),
        created() {
            const repoId = this.$javalin.pathParams["repo-id"];
            if (repoId) {
                fetch(`/api/v1/repos/${repoId}`)
                    .then(res => res.json())
                    .then(res => this.repo = res)
                    .catch(() => "")
            }
        },
        methods: {
            addRepo(evt) {
                evt.preventDefault();
                axios.post("/api/v1/repo", this.repo).then(() => {
                    this.$bvToast.toast("Nice one", {
                        title: 'Success',
                        autoHideDelay: 5000,
                        variant: 'success'
                    });
                    clearInput()
                }).catch((error) => {
                    this.$bvToast.toast("" + error, {
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

    });
</script>
<style>

</style>
