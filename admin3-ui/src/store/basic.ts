import {defineStore} from 'pinia';
import {fetchUserinfo} from "../api";
import {getFileUrl} from "../api/storage";

interface UserStore {
    userinfo: {
        token: string;
        userId: number | null;
        username: string;
        avatar: string;
        avatarUrl: string;
        credential: { identifier: string, identityType: IdentityType };
        permissions: [];
    }
}

enum IdentityType {
    PASSWORD = "PASSWORD"
}

export const useBasicStore = defineStore('basic', {
    state: () => <UserStore>({
        userinfo: {
            token: localStorage.getItem("token"),
            userId: null,
            username: '',
            avatar: '',
            avatarUrl: '',
            credential: {},
            permissions: []
        }
    }),
    actions: {
        setUserinfo(d: any) {
            this.userinfo = d;
            this.userinfo.avatarUrl = getFileUrl(this.userinfo.avatar);
        },
        fetchUserinfo(): Promise<any> {
            return new Promise((resolve, reject) => {
                if (this.userinfo.userId) {
                    resolve(this)
                } else {
                    fetchUserinfo()
                        .then((response: any) => {
                            this.setUserinfo(response.data)
                            resolve(response)
                        })
                        .catch((error: any) => {
                            reject(error)
                        })
                }
            })
        }
    }
});
