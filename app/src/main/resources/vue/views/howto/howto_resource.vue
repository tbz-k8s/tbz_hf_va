<template id="howto-resource">
    <div>
        <navigation active_navigation="Resource"></navigation>
        <b-container>
            <div v-html="html"/>
        </b-container>
    </div>
</template>
<script>
    Vue.component("howto-resource", {
        template: "#howto-resource",
        data: () => ({
            html: ""
        }),
        created() {
            axios.get("https://raw.githubusercontent.com/nliechti/tbz_hf_va/master/docs/howto/HowToResource.md")
                .then(res => {
                    this.html = marked(res.data);
                });
        }
    });

</script>
