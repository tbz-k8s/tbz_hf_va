<template id="github-repos">
    <div>
        <navigation active_navigation="Github Repos"></navigation>
        <h1>All Repositories</h1>
        <b-table hover :items="repos" :fields="rows">

        </b-table>
        <b-button href="/settings/repo" variant="success">Add Repository</b-button>
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
