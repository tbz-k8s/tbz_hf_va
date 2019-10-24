    import Vue from 'vue'

    import bAlert from '/webjars/bootstrap-vue/2.0.4/src/components/alert/alert'
    import bBadge from '/webjars/bootstrap-vue/2.0.4/src/components/badge/badge'
    import bBreadcrumb from '/webjars/bootstrap-vue/2.0.4/src/components/breadcrumb/breadcrumb'
    import bBreadcrumbItem from '/webjars/bootstrap-vue/2.0.4/src/components/breadcrumb/breadcrumb-item'
    import bBreadcrumbLink from '/webjars/bootstrap-vue/2.0.4/src/components/breadcrumb/breadcrumb-link'
    import bButton from '/webjars/bootstrap-vue/2.0.4/src/components/button/button'
    import bButtonClose from '/webjars/bootstrap-vue/2.0.4/src/components/button/button-close'
    import bButtonGroup from '/webjars/bootstrap-vue/2.0.4/src/components/button-group/button-group'
    import bButtonToolbar from '/webjars/bootstrap-vue/2.0.4/src/components/button-toolbar/button-toolbar'
    import bCard from '/webjars/bootstrap-vue/2.0.4/src/components/card/card'
    import bCardBody from '/webjars/bootstrap-vue/2.0.4/src/components/card/card-body'
    import bCardFooter from '/webjars/bootstrap-vue/2.0.4/src/components/card/card-footer'
    import bCardGroup from '/webjars/bootstrap-vue/2.0.4/src/components/card/card-group'
    import bCardHeader from '/webjars/bootstrap-vue/2.0.4/src/components/card/card-header'
    import bCardImage from '/webjars/bootstrap-vue/2.0.4/src/components/card/card-img'
    import bCarousel from '/webjars/bootstrap-vue/2.0.4/src/components/carousel/carousel'
    import bCarouselSlide from '/webjars/bootstrap-vue/2.0.4/src/components/carousel/carousel-slide'
    import bCol from '/webjars/bootstrap-vue/2.0.4/src/components/layout/col'
    import bCollapse from '/webjars/bootstrap-vue/2.0.4/src/components/collapse/collapse'
    import bContainer from '/webjars/bootstrap-vue/2.0.4/src/components/layout/container'
    import bDropdown from '/webjars/bootstrap-vue/2.0.4/src/components/dropdown/dropdown'
    import bDropdownDivider from '/webjars/bootstrap-vue/2.0.4/src/components/dropdown/dropdown-divider'
    import bDropdownHeader from '/webjars/bootstrap-vue/2.0.4/src/components/dropdown/dropdown-header'
    import bDropdownItem from '/webjars/bootstrap-vue/2.0.4/src/components/dropdown/dropdown-item'
    import bDropdownItemButton from '/webjars/bootstrap-vue/2.0.4/src/components/dropdown/dropdown-item-button'
    import bEmbed from '/webjars/bootstrap-vue/2.0.4/src/components/embed/embed'
    import bForm from '/webjars/bootstrap-vue/2.0.4/src/components/form/form'
    import bFormCheckbox from '/webjars/bootstrap-vue/2.0.4/src/components/form-checkbox/form-checkbox'
    import bFormCheckboxGroup from '/webjars/bootstrap-vue/2.0.4/src/components/form-checkbox/form-checkbox-group'
    import bFormFile from '/webjars/bootstrap-vue/2.0.4/src/components/form-file/form-file'
    import bFormGroup from '/webjars/bootstrap-vue/2.0.4/src/components/form-group/form-group'
    import bFormInput from '/webjars/bootstrap-vue/2.0.4/src/components/form-input/form-input'
    import bFormInvalidFeedback from '/webjars/bootstrap-vue/2.0.4/src/components/form/form-invalid-feedback'
    import bFormRadio from '/webjars/bootstrap-vue/2.0.4/src/components/form-radio/form-radio'
    import bFormRadioGroup from '/webjars/bootstrap-vue/2.0.4/src/components/form-radio/form-radio-group'
    import bFormRow from '/webjars/bootstrap-vue/2.0.4/src/components/layout/form-row'
    import bFormSelect from '/webjars/bootstrap-vue/2.0.4/src/components/form-select/form-select'
    import bFormText from '/webjars/bootstrap-vue/2.0.4/src/components/form/form-text'
    import bFormTextarea from '/webjars/bootstrap-vue/2.0.4/src/components/form-textarea/form-textarea'
    import bFormValidFeedback from '/webjars/bootstrap-vue/2.0.4/src/components/form/form-valid-feedback'
    import bImage from '/webjars/bootstrap-vue/2.0.4/src/components/image/img'
    import bImageLazy from '/webjars/bootstrap-vue/2.0.4/src/components/image/img-lazy'
    import bInputGroup from '/webjars/bootstrap-vue/2.0.4/src/components/input-group/input-group'
    import bInputGroupAddon from '/webjars/bootstrap-vue/2.0.4/src/components/input-group/input-group-addon'
    import bInputGroupAppend from '/webjars/bootstrap-vue/2.0.4/src/components/input-group/input-group-append'
    import bInputGroupPrepend from '/webjars/bootstrap-vue/2.0.4/src/components/input-group/input-group-prepend'
    import bInputGroupText from '/webjars/bootstrap-vue/2.0.4/src/components/input-group/input-group-text'
    import bJumbotron from '/webjars/bootstrap-vue/2.0.4/src/components/jumbotron/jumbotron'
    import bLink from '/webjars/bootstrap-vue/2.0.4/src/components/link/link'
    import bListGroup from '/webjars/bootstrap-vue/2.0.4/src/components/list-group/list-group'
    import bListGroupItem from '/webjars/bootstrap-vue/2.0.4/src/components/list-group/list-group-item'
    import bMedia from '/webjars/bootstrap-vue/2.0.4/src/components/media/media'
    import bMediaAside from '/webjars/bootstrap-vue/2.0.4/src/components/media/media-aside'
    import bMediaBody from '/webjars/bootstrap-vue/2.0.4/src/components/media/media-body'
    import bModal from '/webjars/bootstrap-vue/2.0.4/src/components/modal/modal'
    import bNav from '/webjars/bootstrap-vue/2.0.4/src/components/nav/nav'
    import bNavbar from '/webjars/bootstrap-vue/2.0.4/src/components/navbar/navbar'
    import bNavbarBrand from '/webjars/bootstrap-vue/2.0.4/src/components/navbar/navbar-brand'
    import bNavbarNav from '/webjars/bootstrap-vue/2.0.4/src/components/navbar/navbar-nav'
    import bNavbarToggle from '/webjars/bootstrap-vue/2.0.4/src/components/navbar/navbar-toggle'
    import bNavForm from '/webjars/bootstrap-vue/2.0.4/src/components/nav/nav-form'
    import bNavItem from '/webjars/bootstrap-vue/2.0.4/src/components/nav/nav-item'
    import bNavItemDropdown from '/webjars/bootstrap-vue/2.0.4/src/components/nav/nav-item-dropdown'
    import bNavText from '/webjars/bootstrap-vue/2.0.4/src/components/nav/nav-text'
    import bPagination from '/webjars/bootstrap-vue/2.0.4/src/components/pagination/pagination'
    import bPaginationNav from '/webjars/bootstrap-vue/2.0.4/src/components/pagination-nav/pagination-nav'
    import bPopover from '/webjars/bootstrap-vue/2.0.4/src/components/popover/popover'
    import bProgress from '/webjars/bootstrap-vue/2.0.4/src/components/progress/progress'
    import bProgressBar from '/webjars/bootstrap-vue/2.0.4/src/components/progress/progress-bar'
    import bRow from '/webjars/bootstrap-vue/2.0.4/src/components/layout/row'
    import bTab from '/webjars/bootstrap-vue/2.0.4/src/components/tabs/tab'
    import bTable from '/webjars/bootstrap-vue/2.0.4/src/components/table/table'
    import bTabs from '/webjars/bootstrap-vue/2.0.4/src/components/tabs/tabs'
    import bTooltip from '/webjars/bootstrap-vue/2.0.4/src/components/tooltip/tooltip'

    Vue.component('b-alert', bAlert)
    Vue.component('b-badge', bBadge)
    Vue.component('b-breadcrumb', bBreadcrumb)
    Vue.component('b-breadcrumb-item', bBreadcrumbItem)
    Vue.component('b-breadcrumb-link', bBreadcrumbLink)
    Vue.component('b-button', bButton)
    Vue.component('b-button-close', bButtonClose)
    Vue.component('b-button-group', bButtonGroup)
    Vue.component('b-button-toolbar', bButtonToolbar)
    Vue.component('b-card', bCard)
    Vue.component('b-card-body', bCardBody)
    Vue.component('b-card-footer', bCardFooter)
    Vue.component('b-card-group', bCardGroup)
    Vue.component('b-card-header', bCardHeader)
    Vue.component('b-card-image', bCardImage)
    Vue.component('b-carousel', bCarousel)
    Vue.component('b-carousel-slide', bCarouselSlide)
    Vue.component('b-col', bCol)
    Vue.component('b-collapse', bCollapse)
    Vue.component('b-container', bContainer)
    Vue.component('b-dropdown', bDropdown)
    Vue.component('b-dropdown-divider', bDropdownDivider)
    Vue.component('b-dropdown-header', bDropdownHeader)
    Vue.component('b-dropdown-item', bDropdownItem)
    Vue.component('b-dropdown-item-button', bDropdownItemButton)
    Vue.component('b-embed', bEmbed)
    Vue.component('b-form', bForm)
    Vue.component('b-form-checkbox', bFormCheckbox)
    Vue.component('b-form-checkbox-group', bFormCheckboxGroup)
    Vue.component('b-form-file', bFormFile)
    Vue.component('b-form-group', bFormGroup)
    Vue.component('b-form-input', bFormInput)
    Vue.component('b-form-invalid-feedback', bFormInvalidFeedback)
    Vue.component('b-form-radio', bFormRadio)
    Vue.component('b-form-radio-group', bFormRadioGroup)
    Vue.component('b-form-row', bFormRow)
    Vue.component('b-form-row', bFormRow)
    Vue.component('b-form-select', bFormSelect)
    Vue.component('b-form-text', bFormText)
    Vue.component('b-form-textarea', bFormTextarea)
    Vue.component('b-form-valid-feedback', bFormValidFeedback)
    Vue.component('b-image', bImage)
    Vue.component('b-image-lazy', bImageLazy)
    Vue.component('b-input-group', bInputGroup)
    Vue.component('b-input-group-addon', bInputGroupAddon)
    Vue.component('b-input-group-append', bInputGroupAppend)
    Vue.component('b-input-group-prepend', bInputGroupPrepend)
    Vue.component('b-input-group-text', bInputGroupText)
    Vue.component('b-jumbotron', bJumbotron)
    Vue.component('b-link', bLink)
    Vue.component('b-list-group', bListGroup)
    Vue.component('b-list-group-item', bListGroupItem)
    Vue.component('b-media', bMedia)
    Vue.component('b-media-aside', bMediaAside)
    Vue.component('b-media-body', bMediaBody)
    Vue.component('b-modal', bModal)
    Vue.component('b-nav', bNav)
    Vue.component('b-nav-form', bNavForm)
    Vue.component('b-nav-item', bNavItem)
    Vue.component('b-nav-item-dropdown', bNavItemDropdown)
    Vue.component('b-nav-text', bNavText)
    Vue.component('b-navbar', bNavbar)
    Vue.component('b-navbar-brand', bNavbarBrand)
    Vue.component('b-navbar-nav', bNavbarNav)
    Vue.component('b-navbar-toggle', bNavbarToggle)
    Vue.component('b-pagination', bPagination)
    Vue.component('b-pagination-nav', bPaginationNav)
    Vue.component('b-popover', bPopover)
    Vue.component('b-progress', bProgress)
    Vue.component('b-progress-bar', bProgressBar)
    Vue.component('b-row', bRow)
    Vue.component('b-tab', bTab)
    Vue.component('b-table', bTable)
    Vue.component('b-tabs', bTabs)
    Vue.component('b-tooltip', bTooltip)
