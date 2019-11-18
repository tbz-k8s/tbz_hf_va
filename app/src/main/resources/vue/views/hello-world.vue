<template id="hello-world">
    <div>
        <navigation active_navigation="Home"></navigation>
        <b-container>
            <h1>Use TBZ Deployer</h1>
        </b-container>
    </div>
</template>
<script>
    Vue.component("hello-world", {
        template: "#hello-world",
        data: () => ({
            html: ""
        }),
        created() {
            
        }
    });

</script>
