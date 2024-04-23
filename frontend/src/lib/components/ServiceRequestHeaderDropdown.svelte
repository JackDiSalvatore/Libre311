<script lang="ts" context="module">
	let cachedServiceList: GetServiceListResponse | undefined = undefined;
</script>

<script lang="ts">
	import messages from '$media/messages.json';
	import Funnel from './Svg/outline/Funnel.svelte';
	import { magnifingGlassIcon } from './Svg/outline/magnifyingGlassIcon';
	import { calendarIcon } from './Svg/outline/calendarIcon';
	import { Button, DatePicker, Dropdown, Input, Select } from 'stwui';
	import Pagination from '$lib/components/Pagination.svelte';
	import {
		type GetServiceListResponse,
		type ServiceRequestPriority,
		type ServiceRequestStatus
	} from '$lib/services/Libre311/Libre311';
	import {
		ASYNC_IN_PROGRESS,
		asAsyncSuccess,
		type AsyncResult,
		asAsyncFailure
	} from '$lib/services/http';
	import type { SelectOption } from 'stwui/types';
	import { useLibre311Context, useLibre311Service } from '$lib/context/Libre311Context';
	import { useServiceRequestsContext } from '$lib/context/ServiceRequestsContext';
	import { page } from '$app/stores';
	import {
		serviceRequestPrioritySelectOptions,
		serviceRequestStatusSelectOptions
	} from '$lib/utils/functions';
	import { onMount } from 'svelte';

	const libre311 = useLibre311Service();
	const linkResolver = useLibre311Context().linkResolver;
	const ctx = useServiceRequestsContext();
	const serviceRequestsRes = ctx.serviceRequestsResponse;

	let selectedServicePriority: ServiceRequestPriority[];
	let selectedServiceCodes: string[] | undefined;
	let serviceList: AsyncResult<GetServiceListResponse> = ASYNC_IN_PROGRESS;
	let statusInput: ServiceRequestStatus[];
	let startDate: Date;
	let endDate: Date;
	let isDropDownVisable: boolean = false;

	function createSelectOptions(res: GetServiceListResponse): SelectOption[] {
		return res.map((s) => ({ value: s.service_code, label: s.service_name }));
	}

	async function handleSearchInput(e: Event) {
		console.log(e);
		const target = e.target as HTMLInputElement;

		// Remove non-numeric characters from the input value
		let sanitizedValue = target.value.replace(/\D/g, '');

		if (sanitizedValue) {
			const serviceRequestId = Number(sanitizedValue);
			ctx.applyServiceRequestParams([serviceRequestId], $page.url);
		} else {
			ctx.applyServiceRequestParams({}, $page.url);
		}
	}

	function fetchServiceList() {
		if (cachedServiceList) {
			serviceList = asAsyncSuccess(cachedServiceList);
			return;
		}
		libre311
			.getServiceList()
			.then((res) => {
				cachedServiceList = res;
				serviceList = asAsyncSuccess(res);
			})
			.catch((err) => (serviceList = asAsyncFailure(err)));
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

	function toggleDropdown() {
		isDropDownVisable = !isDropDownVisable;
	}

	onMount(fetchServiceList);

	$: handleFilterInput(
		selectedServicePriority,
		selectedServiceCodes,
		statusInput,
		startDate,
		endDate
	);
</script>

{#if $serviceRequestsRes.type === 'success'}
	<div class="mx-2 flex items-center justify-between">
		<div class="mx-2 flex items-center justify-between">
			<div class="mr-4">
				<h3 class="text-base">{messages['sidebar']['title']}</h3>
			</div>

			<div>
				<Pagination
					pagination={$serviceRequestsRes.value.metadata.pagination}
					nextPage={linkResolver.nextIssuesTablePage(
						$serviceRequestsRes.value.metadata.pagination,
						$page.url
					)}
					prevPage={linkResolver.prevIssuesTablePage(
						$serviceRequestsRes.value.metadata.pagination,
						$page.url
					)}
				/>
			</div>
		</div>

		<div class="mx-2 flex items-center">
			<!-- <Input placeholder="Request ID" on:change={handleSearchInput}>
				<Input.Trailing slot="trailing" data={magnifingGlassIcon} />
			</Input> -->

			<Dropdown bind:visible={isDropDownVisable}>
				<Button slot="trigger" type="ghost" on:click={toggleDropdown}>
					<Funnel slot="icon" />
				</Button>

				<Dropdown.Items slot="items" alignment="end" id="dropdown-override">
					<div class="m-2">
						<h3 class="text-left text-base">Search By ID:</h3>

						<Input placeholder="Request ID" on:change={handleSearchInput}>
							<Input.Trailing slot="trailing" data={magnifingGlassIcon} />
						</Input>
					</div>

					<Dropdown.Items.Divider class="my-4" />

					<h3 class="m-2 text-left text-base">Search By Filters:</h3>

					<div class="m-2">
						<Select
							bind:value={selectedServicePriority}
							name="select-priority"
							placeholder="Priority"
							multiple
							options={serviceRequestPrioritySelectOptions}
						>
							<Select.Options slot="options">
								{#each serviceRequestPrioritySelectOptions as option}
									<Select.Options.Option {option} />
								{/each}
							</Select.Options>
						</Select>
					</div>

					<div class="m-2">
						<Select
							name="select-status"
							placeholder="Status"
							multiple
							options={serviceRequestStatusSelectOptions}
							bind:value={statusInput}
						>
							<Select.Options slot="options">
								{#each serviceRequestStatusSelectOptions as option}
									<Select.Options.Option {option} />
								{/each}
							</Select.Options>
						</Select>
					</div>

					{#if serviceList.type === 'success'}
						{@const selectOptions = createSelectOptions(serviceList.value)}
						<div class="m-2 min-w-52">
							<Select
								bind:value={selectedServiceCodes}
								name="select-1"
								placeholder="Request Type"
								multiple
								options={selectOptions}
							>
								<Select.Options slot="options">
									{#each selectOptions as option}
										<Select.Options.Option {option} />
									{/each}
								</Select.Options>
							</Select>
						</div>
					{/if}

					<div class="m-2">
						<DatePicker
							name="start-datetime"
							allowClear
							bind:value={startDate}
							id="date-picker-override"
						>
							<DatePicker.Label class="text-left" slot="label">Reported From</DatePicker.Label>
							<DatePicker.Leading slot="leading" data={calendarIcon} />
						</DatePicker>
					</div>

					<div class="m-2">
						<DatePicker name="end-datetime" allowClear bind:value={endDate}>
							<DatePicker.Label class="text-left" slot="label">Reported To</DatePicker.Label>
							<DatePicker.Leading slot="leading" data={calendarIcon} />
						</DatePicker>
					</div>
				</Dropdown.Items>
			</Dropdown>
		</div>
	</div>
{:else}
	{'loading...'}
{/if}

<style>
	:global(#dropdown-override) {
		height: 450px;
		min-height: 450px;
		min-width: 350px;
	}

	:global(#date-picker-override) {
		z-index: 100;
	}
</style>
