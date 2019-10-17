<template id="github-repos">
    <navigation active_navigation="Github Repos"></navigation>

</template>
<script>
    Vue.component("github-repos", {
        template: "#github-repos",
        data: () => ({
            repos: [],
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
