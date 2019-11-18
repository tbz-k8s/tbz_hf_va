<template id="howto-deploy">
    <div>
        <navigation active_navigation="Deploy"></navigation>
        <b-container>
            <div v-html="html"/>
        </b-container>
    </div>
</template>
<script>
    Vue.component("howto-deploy", {
        template: "#howto-deploy",
        data: () => ({
            html: ""
        }),
        created() {
            axios.get("https://raw.githubusercontent.com/nliechti/tbz_hf_va/master/docs/howto/howto_tbz_deployer.md")
                .then(res => {
                    this.html = marked(res.data);
                });
        }
    });

</script>
