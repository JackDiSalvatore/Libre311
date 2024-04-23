<script lang="ts">
	import ServiceRequestPreview from '$lib/components/ServiceRequestPreview.svelte';
	import { page } from '$app/stores';
	import { useServiceRequestsContext } from '$lib/context/ServiceRequestsContext';
	import { useLibre311Context } from '$lib/context/Libre311Context';
	import MapListToggle from '$lib/components/MapListToggle.svelte';
	import ServiceRequestHeaderDropdown from '$lib/components/ServiceRequestHeaderDropdown.svelte';

	const ctx = useServiceRequestsContext();
	const serviceRequestsRes = ctx.serviceRequestsResponse;

	const linkResolver = useLibre311Context().linkResolver;

	let listElement: HTMLElement;
</script>

<div class="flex items-center justify-center">
	<div class="w-full max-w-lg">
		<ServiceRequestHeaderDropdown />

		<div class="my-4 flex justify-center">
			<MapListToggle />
		</div>

		{#if $serviceRequestsRes.type === 'success'}
			<div bind:this={listElement}>
				<ul>
					{#each $serviceRequestsRes.value.serviceRequests as serviceRequest}
						<li class="m-3">
							<ServiceRequestPreview
								{serviceRequest}
								detailsLink={linkResolver.issueDetailsMobile(
									$page.url,
									serviceRequest.service_request_id
								)}
							/>
						</li>
					{/each}
				</ul>
			</div>
		{/if}
	</div>
</div>
