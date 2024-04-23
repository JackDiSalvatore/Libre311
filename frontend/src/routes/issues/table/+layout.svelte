<script lang="ts">
	import SideBarMainContentLayout from '$lib/components/SideBarMainContentLayout.svelte';
	import { Button, Card, Table } from 'stwui';
	import { page } from '$app/stores';
	import { useLibre311Context, useLibre311Service } from '$lib/context/Libre311Context';
	import {
		useSelectedServiceRequestStore,
		useServiceRequestsContext
	} from '$lib/context/ServiceRequestsContext';
	import { goto } from '$app/navigation';
	import { saveAs } from 'file-saver';
	import { arrowDownTray } from '$lib/components/Svg/outline/arrowDownTray';
	import {
		type ServiceRequest,
		type ServiceRequestId,
		type ServiceRequestPriority,
		type ServiceRequestStatus
	} from '$lib/services/Libre311/Libre311';
	import { toAbbreviatedTimeStamp } from '$lib/utils/functions';
	import type { Maybe } from '$lib/utils/types';
	import { columns } from './table';
	import ServiceRequestStatusBadge from '$lib/components/ServiceRequestStatusBadge.svelte';
	import { FilteredServiceRequestsParamsMapper } from '$lib/services/Libre311/FilteredServiceRequestsParamsMapper';
	import ServiceRequestHeaderDropdown from '$lib/components/ServiceRequestHeaderDropdown.svelte';
	import ServiceRequestHeaderFlat from '$lib/components/ServiceRequestHeaderFlat.svelte';

	const linkResolver = useLibre311Context().linkResolver;
	const selectedServiceRequestStore = useSelectedServiceRequestStore();
	const ctx = useServiceRequestsContext();
	const libre311 = useLibre311Service();
	const serviceRequestsRes = ctx.serviceRequestsResponse;

	let selectedServicePriority: ServiceRequestPriority[];
	let selectedServiceCodes: string[] | undefined;
	let statusInput: ServiceRequestStatus[];
	let orderBy: string;
	let startDate: Date;
	let endDate: Date;

	function selectRow(service_request_id: ServiceRequestId) {
		goto(linkResolver.issueDetailsTable($page.url, service_request_id));
		return;
	}

	function resolveStyleId(
		serviceRequest: ServiceRequest,
		selectedServiceRequest: Maybe<ServiceRequest>
	) {
		return serviceRequest.service_request_id === selectedServiceRequest?.service_request_id
			? 'selected'
			: 'item-id';
	}

	async function handleDownloadCsv() {
		const serviceRequestsBlob = await libre311.downloadServiceRequests(
			FilteredServiceRequestsParamsMapper.toRequestParams($page.url.searchParams)
		);

		saveAs(serviceRequestsBlob, 'service-requests.csv');
	}

	async function handleFilterInput(
		selectedServicePriority: ServiceRequestPriority[],
		selectedServiceCodes: string[] | undefined,
		statusInput: ServiceRequestStatus[],
		startDate: Date,
		endDate: Date
	) {
		ctx.applyServiceRequestParams(
			{
				servicePriority: selectedServicePriority,
				serviceCode: selectedServiceCodes?.map((s) => Number(s)),
				status: statusInput,
				startDate: startDate?.toISOString(),
				endDate: endDate?.toISOString()
			},
			$page.url
		);
	}

	$: handleFilterInput(
		selectedServicePriority,
		selectedServiceCodes,
		statusInput,
		startDate,
		endDate
	);
</script>

{#if $serviceRequestsRes.type === 'success'}
	<SideBarMainContentLayout>
		<ServiceRequestHeaderFlat slot="header-content" />

		<slot slot="side-bar" />
		<div slot="main-content" class="relative flex h-full flex-col">
			<Card bordered={true} class="m-2">
				<Card.Content slot="content" class="p-0 sm:p-0">
					<div class="issues-table-override">
						<Table class="h-full overflow-hidden rounded-md" {columns}>
							<Table.Header slot="header" {orderBy} />
							<Table.Body slot="body">
								{#each $serviceRequestsRes.value.serviceRequests as item}
									<Table.Body.Row
										id={resolveStyleId(item, $selectedServiceRequestStore)}
										on:click={() => selectRow(item.service_request_id)}
									>
										<Table.Body.Row.Cell column={0}>
											<div class="flex items-center justify-center">
												{item.service_request_id}
											</div>
										</Table.Body.Row.Cell>

										<Table.Body.Row.Cell column={1}>
											<div class="flex items-center justify-center">
												{item.priority
													? `${item.priority.charAt(0).toUpperCase()}${item.priority.slice(1)}`
													: '--'}
											</div>
										</Table.Body.Row.Cell>

										<Table.Body.Row.Cell column={2}>
											<div class="flex items-center justify-center">
												{item.service_name}
											</div>
										</Table.Body.Row.Cell>

										<Table.Body.Row.Cell column={3}>
											<div class="flex items-center justify-center">
												<ServiceRequestStatusBadge status={item.status} />
											</div>
										</Table.Body.Row.Cell>

										<Table.Body.Row.Cell column={4}>
											<div class="flex items-center justify-center">
												<p
													class="w-24 overflow-hidden text-ellipsis whitespace-nowrap text-sm 2xl:w-32"
												>
													{item.address}
												</p>
											</div>
										</Table.Body.Row.Cell>

										<Table.Body.Row.Cell column={5}>
											<div class="flex items-center justify-center">
												{toAbbreviatedTimeStamp(item.requested_datetime)}
											</div>
										</Table.Body.Row.Cell>

										<Table.Body.Row.Cell column={6}>
											<div class="flex items-center justify-center">
												{#if item.expected_datetime}
													{toAbbreviatedTimeStamp(item.expected_datetime)}
												{:else}
													--
												{/if}
											</div>
										</Table.Body.Row.Cell>
									</Table.Body.Row>
								{/each}
							</Table.Body>

							<Table.Footer slot="footer">
								<div class="m-2 flex justify-end">
									<Button on:click={handleDownloadCsv}>
										Download CSV
										<Button.Trailing data={arrowDownTray} slot="trailing" />
									</Button>
								</div>
							</Table.Footer>
						</Table>
					</div>
				</Card.Content>
			</Card>
		</div>
	</SideBarMainContentLayout>
{:else if $serviceRequestsRes.type === 'failure'}
	{JSON.stringify($serviceRequestsRes, null, 2)}
{/if}

<style>
	.issues-table-override :global(#selected) {
		--tw-bg-opacity: 0.15;
		background-color: hsl(var(--primary) / var(--tw-bg-opacity));
	}

	.issues-table-override :global(#item-id):hover {
		--tw-bg-opacity: 0.15;
		background-color: hsl(var(--primary) / var(--tw-bg-opacity));
	}
</style>
