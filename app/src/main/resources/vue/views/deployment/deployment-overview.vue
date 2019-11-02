<template id="deployments">
    <div>
        <navigation active_navigation="Deployments"></navigation>
        <b-container>
            <h1>Existing Deployments</h1>
            <b-table hover :items="deployments" :fields="rows">

            </b-table>
            <b-row>
                <b-col>
                    <b-button href="" variant="success" class="float-right">Add Deployment</b-button>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>

<script>
    Vue.component("deployments", {
        template: "#deployments",
        data: () => ({
            deployments: [],
            rows: [
                {
                    key: 'name',
                    sortable: true
                },
                {
                    key: 'replications',
                    sortable: true
                }
            ]
        }),
        created() {
            fetch("/api/v1/deployments")
                .then(res => res.json())
                .then(res => this.deploymens = res)
                .catch(() => "")
        }
    });
</script>

<style scoped>

</style>