<template id="nav-back">
    <b-button variant="outline-primary" :href="return_to"><-</b-button>
</template>
<script>
    Vue.component("nav-back", {template: "#nav-back", props: ['return_to']});
</script>
