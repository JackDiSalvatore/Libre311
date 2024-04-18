<script lang="ts">
	import messages from '$media/messages.json';
	import ServiceRequestPreview from '$lib/components/ServiceRequestPreview.svelte';

	import Pagination from '$lib/components/Pagination.svelte';
	import { useLibre311Context } from '$lib/context/Libre311Context';
	import { page } from '$app/stores';
	import {
		useServiceRequestsContext,
		useServiceRequestsResponseStore
	} from '$lib/context/ServiceRequestsContext';

	const ctx = useServiceRequestsContext();
	const serviceRequestsRes = ctx.serviceRequestsResponse;

	const linkResolver = useLibre311Context().linkResolver;

	const serviceRequestResStore = useServiceRequestsResponseStore();
	let listElement: HTMLElement | undefined;

	$: if ($serviceRequestResStore.type === 'success') listElement?.scrollIntoView();
</script>

{#if $serviceRequestsRes.type === 'success'}
	<div bind:this={listElement}>
		<ul>
			{#each $serviceRequestsRes.value.serviceRequests as serviceRequest}
				<li class="m-3">
					<ServiceRequestPreview
						{serviceRequest}
						detailsLink={linkResolver.issueDetailsTable(
							$page.url,
							serviceRequest.service_request_id
						)}
					/>
				</li>
			{/each}
		</ul>
	</div>
{/if}
