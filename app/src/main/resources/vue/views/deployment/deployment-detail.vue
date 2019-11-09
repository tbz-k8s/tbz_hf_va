<template id="deployment-detail">
    <div>
        <navigation active_navigation="Deployment Detail"></navigation>
        <b-container>
            <h1>{{deploymentName}}</h1>
        </b-container>
    </div>
</template>

<script>
    Vue.component("deployment-detail", {
        template: "#deployment-detail",
        data: () => ({
            deploymentName: "",
            deployments: [{}]
        }),
        created() {
            const deploymentName = this.$javalin.pathParams["deployment-name"];
            this.deploymentName = deploymentName;
            fetch(`/api/v1/deployment/${deploymentName}`)
                .then(res => res.json())
                .then(res => this.deployments = res)
                .catch(() => "")
        },
        methods: {}
    });
</script>

<style scoped>

</style>