<template id="navigation" xmlns="">
    <div>
        <b-navbar toggleable="lg" type="dark" variant="dark">
            <b-navbar-brand href="/">Tbz Deployer</b-navbar-brand>
            <b-navbar-brand href="#">{{ active_navigation }}</b-navbar-brand>
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item-dropdown text="Settings">
                        <b-dropdown-item href="/settings/repos">Github Repos</b-dropdown-item>
                        <b-dropdown-item href="/settings/school-classes">Klassen</b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-item-dropdown text="HowTo">
                        <b-dropdown-item href="/howto/tbz-deployer">Use Deployer</b-dropdown-item>
                        <b-dropdown-item href="/howto/resource">Create resources</b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-item href="/deployments">Deployments</b-nav-item>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
    </div>
</template>
<script>
    Vue.component("navigation", {
        template: "#navigation",
        props: ['active_navigation'],
        data: () => ({
            drawer: null,
        }),
    });
</script>
<style>
    .navigation {
        color: goldenrod;
    }
</style>
