<template id="github-repos">
    <div>
        <navigation active_navigation="Github Repos"></navigation>
        <b-container>
            <h1>All Repositories</h1>
            <b-table hover :items="repos" :fields="rows">

            </b-table>
            <b-row>
                <b-col>
                    <b-button href="/settings/repo" variant="success" class="float-right">Add Repository</b-button>
                </b-col>
            </b-row>
        </b-container>
    </div>

</template>
<script>
    Vue.component("github-repos", {
        template: "#github-repos",
        data: () => ({
            repos: [],
            rows: [
                {
                    key: 'name',
                    sortable: true
                },
                {
                    key: 'url',
                    sortable: true
                }, {
                    key: 'Action',
                    sortable: false
                }]
        }),
        created() {
            fetch("/api/v1/repos")
                .then(res => res.json())
                .then(res => this.repos = res)
                .catch(() => "")
        }
    });
</script>
<style>
    .github-repos {
        color: goldenrod;
    }
</style>
