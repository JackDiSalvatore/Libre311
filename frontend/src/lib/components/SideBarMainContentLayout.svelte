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
		text-align: center;
		min-height: 30px;
	}

	.split-layout-container {
		/* grid-template-columns: 100%; */
		--header-height: 6rem;
		/* border: 1px solid hsl(var(--primary)); */
		height: calc(100dvh - var(--header-height));
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
