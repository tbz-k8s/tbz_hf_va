<template id="github-repos">
    <div>
        <navigation active_navigation="Github Repos"></navigation>
        <b-container>
            <h1>All Repositories</h1>
            <b-table hover :items="repos" :fields="rows">
                <template v-slot:cell(action)="data">
                    <div class="float-right">
                        <b-button @click="deleteRepo(data.item.id)" variant="outline-danger">
                            X
                        </b-button>
                    </div>
                </template>
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
                    key: 'action',
                    sortable: false
                }]
        }),
        created() {
            this.loadData();
            setInterval(function () {
                this.loadData();
            }.bind(this), 2000);
        },
        methods: {
            deleteRepo(id) {
                axios.delete(`/api/v1/repos/${id}`)
            },
            loadData() {
                fetch("/api/v1/repos")
                    .then(res => res.json())
                    .then(res => this.repos = res)
                    .catch(() => "")
            }
        }
    });
</script>
<style>
    .github-repos {
        color: goldenrod;
    }
</style>
