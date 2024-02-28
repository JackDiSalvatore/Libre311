import type { SelectOption, TableColumn } from 'stwui/types';

// 14% * 7 = 98%
export const columns: TableColumn[] = [
	{
		column: 'issue_id',
		label: 'Issue ID',
		placement: 'center',
		class: 'w-[14%]'
	},
	{
		column: 'service_name',
		label: 'Service Name',
		placement: 'center',
		class: 'w-[14%]'
	},
	{
		column: 'status',
		label: 'Status',
		placement: 'center',
		class: 'w-[14%]'
	},
	{
		column: 'address',
		label: 'Address',
		placement: 'center',
		class: 'hidden xl:table-cell w-[14%]'
	},
	{
		column: 'created_at',
		label: 'Created',
		placement: 'center',
		class: 'hidden lg:table-cell w-[14%]'
	},
	{
		column: 'last_updated',
		label: 'Last Updated',
		placement: 'center',
		class: 'hidden lg:table-cell w-[14%]'
	},
	{
		column: 'expected_completion',
		label: 'Expected Completion',
		placement: 'center',
		class: 'hidden xl:table-cell w-[14%]'
	}
];

export const priorityOptions: SelectOption[] = [
	{
		value: 'low',
		label: 'Low'
	},
	{
		value: 'medium',
		label: 'Medium'
	},
	{
		value: 'high',
		label: 'High'
	}
];

export const statusOptions: SelectOption[] = [
	{
		value: 'open',
		label: 'Open'
	},
	{
		value: 'closed',
		label: 'Closed'
	}
];