<template id="github-repo">
    <div>
        <navigation :active_navigation="'Repo: ' + repo.name"></navigation>
        <div class="container">
            <b-form @submit="onSubmit" @reset="onReset">
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
                <b-button type="submit" variant="primary">Submit</b-button>
                <b-button type="reset" variant="danger-outline">Reset</b-button>
            </b-form>
        </div>
    </div>

</template>
<script>
    Vue.component("github-repo", {
        template: "#github-repo",
        data: () => ({
            repo: {name: "", url: ""}
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
            onSubmit(evt) {
                console.log("bla");
                axios.post("/api/v1/repos", [this.repo]).then(() => {
                    console.log("bla server")
                })
            }, onReset(evt) {
                this.repo = null
            }
        }

    });
</script>
<style>

</style>
