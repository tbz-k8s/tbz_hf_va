<template id="school-classes">
    <div>
        <navigation active_navigation="School Classes"></navigation>
        <b-container>
            <h1>All Classes</h1>
            <b-table hover :items="classes" :fields="rows">
                <template v-slot:cell(action)="data">
                    <div class="float-right">
                        <b-button :href="'/settings/school-class' + data.item.name" variant="outline-primary">
                            Edit
                        </b-button>
                        <b-button @click="deleteSchoolClass(data.item.name)" variant="outline-danger">
                            X
                        </b-button>
                    </div>
                </template>
            </b-table>
            <b-row>
                <b-col>
                    <b-button href="/settings/school-class" variant="success" class="float-right">Add School Class
                    </b-button>
                </b-col>
            </b-row>
        </b-container>
    </div>

</template>
<script>
    Vue.component("school-classes", {
        template: "#school-classes",
        data: () => ({
            classes: [],
            rows: [
                {
                    key: 'name',
                    sortable: true
                }, {
                    key: 'action',
                    sortable: false
                }]
        }),
        created() {
            this.loadData();
            setInterval(function () {
                this.loadData();
            }.bind(this), 2000);
        },
        methods: {
            deleteSchoolClass(name) {
                axios.delete(`/api/v1/school-class/${name}`)
            },
            loadData() {
                fetch("/api/v1/school-classes")
                    .then(res => res.json())
                    .then(res => this.classes = res)
                    .catch(() => "")
            }
        }
    });
</script>
