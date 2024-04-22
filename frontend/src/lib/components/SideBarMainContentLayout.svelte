<script lang="ts">
	export let sideBarBreakpointActive = true;
</script>

<div class="layout-container">
	<div class="header-content">
		<slot name="header-content" />
	</div>

	<div class="split-layout-container">
		<aside class="side-bar h-full" class:collapsable-side-bar={sideBarBreakpointActive}>
			<slot name="side-bar" />
		</aside>

		<div class="main-content">
			<slot name="main-content" />
		</div>
	</div>
</div>

<style>
	.layout-container {
		height: 100%;
	}

	.header-content {
		height: 10%;
		padding: 10px 0;
		text-align: center;
	}

	.split-layout-container {
		grid-template-columns: 100%;
		height: 90%;
	}

	.main-content {
		flex-grow: 1;
		height: 100%;
	}

	.collapsable-side-bar {
		display: none;
	}

	.side-bar {
		position: relative;
		width: 400px;
	}

	aside {
		overflow-y: auto;
	}

	@media (min-width: 769px) {
		.split-layout-container {
			display: grid;
			grid-template-columns: 400px auto;
		}
		.collapsable-side-bar {
			display: unset;
		}
	}
</style>
