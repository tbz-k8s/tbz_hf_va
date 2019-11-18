<template id="howto-tbz-deployer">
    <div>
        <navigation active_navigation="Use Deployer"></navigation>
        <b-container>
            <div v-html="html"/>
        </b-container>
    </div>
</template>
<script>
    Vue.component("howto-tbz-deployer", {
        template: "#howto-tbz-deployer",
        data: () => ({
            html: ""
        }),
        created() {
            marked.setOptions({
                gfm: true,
                baseUrl: "https://raw.githubusercontent.com/nliechti/tbz_hf_va/master/docs/howto/"
            });
            axios.get("")
                .then(res => {
                    this.html = marked(res.data);
                });
        }
    });

</script>
