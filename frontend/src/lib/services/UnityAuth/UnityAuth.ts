import type { AxiosInstance } from "axios";
import axios from "axios";
import { z } from "zod";

// Auth props schema
const UnityAuthServicePropsSchema = z.object({
	baseURL: z.string()
});

export type UnityAuthServiceProps = z.infer<typeof UnityAuthServicePropsSchema>;

// Auth Login Schema
const UnityAuthLoginResponseSchema = z.object({
	access_token: z.string(),
	token_type: z.string(),
	expires_in: z.number(),
	username: z.string()
});

export type UnityAuthLoginResponse = z.infer<typeof UnityAuthLoginResponseSchema>;

// Auth Interface
export interface UnityAuthService {
	login(email: string, password: string): Promise<UnityAuthLoginResponse>;
}

// Auth Implementation
export class UnityAuthServiceImpl implements UnityAuthService {
	private axiosInstance: AxiosInstance;

	private constructor(props: UnityAuthServiceProps) {
		UnityAuthServicePropsSchema.parse(props);
		this.axiosInstance = axios.create({ baseURL: props.baseURL });

	}

	public static create(props: UnityAuthServiceProps) {
		return new UnityAuthServiceImpl({ ...props });
	}

	async login(email: string, password: string): Promise<UnityAuthLoginResponse> {
		try {
			const res = await this.axiosInstance.post('/login', {
				username: email,
				password: password
			});

			return UnityAuthLoginResponseSchema.parse(res.data);
		} catch (e) {
			throw new Error(e?.toString());
		}
	}

}

// Auth Factory
export function unityAuthServiceFactory(props: UnityAuthServiceProps): UnityAuthService {
	return UnityAuthServiceImpl.create(props);
}