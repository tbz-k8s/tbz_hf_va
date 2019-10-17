<template id="hello-world">
    <div>
        <navigation active_navigation="Home"></navigation>
        <h1>Welcome home</h1>
    </div>
</template>
<script>
    Vue.component("hello-world", {template: "#hello-world"});
</script>
<style>
    .hello-world {
        color: goldenrod;
    }
</style>
