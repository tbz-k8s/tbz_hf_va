<template id="school-class">
    <div>
        <navigation :active_navigation="'School Class'"></navigation>
        <b-container>
            <b-form @submit="addSchoolClass" @reset="onReset">
                <b-form-file
                        v-model="file"
                        :state="Boolean(file)"
                        accept="text/csv"
                        placeholder="Choose a file or drop it here..."
                        drop-placeholder="Drop file here..."
                ></b-form-file>
                <b-row>
                    <b-col>
                        <nav-back return_to="/settings/school-classes"/>
                    </b-col>
                    <b-col>
                        <div class="float-right">
                            <b-button type="submit" variant="primary">Submit</b-button>
                            <b-button type="reset" variant="outline-danger">Reset</b-button>
                        </div>
                    </b-col>
                </b-row>
            </b-form>
        </b-container>
    </div>

</template>
<script>
    function clearInput() {
        this.schoolClass = {name: ""}
    }

    Vue.component("school-class", {
        template: "#school-class",
        data: () => ({
            file: null
        }),
        created() {
            const className = this.$javalin.pathParams["class-name"];
            if (className) {
                fetch(`/api/v1/school-classes/${className}`)
                    .then(res => res.json())
                    .then(res => this.class = res)
                    .catch(() => "")
            }
        },
        methods: {
            addSchoolClass(evt) {
                evt.preventDefault();
                let formData = new FormData();
                formData.append("file", this.file);
                axios.post("/api/v1/school-class", formData).then(() => {
                    this.$bvToast.toast("Nice one", {
                        title: 'Success',
                        autoHideDelay: 5000,
                        variant: 'success'
                    });
                    clearInput()
                }).catch((error) => {
                    this.$bvToast.toast("" + error, {
                        title: 'Error Saving Record',
                        autoHideDelay: 5000,
                        variant: 'danger'
                    })
                })

            },
            onReset(evt) {
                clearInput()
            }
        }

    });
</script>
<style>

</style>
