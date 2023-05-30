# download-ui

## introduce

项目前端使用`Vue3+element-plus+Ts`构建

### show

#### login page

![1](./doc_img/1.png)



#### home page

![2](./doc_img/2.png)



#### parse page

![2](./doc_img/parse_page.png)



#### download page

![parse_page](./doc_img/download_page.png)

![parse_page](./doc_img/download_page2.png)



#### download history page

![parse_page](./doc_img/history_page.png)



#### Svip account manage page

![svip_manage_page.png](./doc_img/svip_manage_page.png)

![svip_manage_page2.png](./doc_img/svip_manage_page2.png)



#### common account manage page

![svip_manage_page2.png](./doc_img/common_account_manage_page.png)



#### user manage page

![svip_manage_page2.png](./doc_img/user_page.png)



## Using Tutorials

#### user

Copy the Baidu sharing link to the parsing page.

After parsing, select the file to be parsed on the download list page

After parsing, you can choose to send the parsing results to Aria2, or download the link file and go to Xunlei to create a new download task.

#### admin

Adding an SVIP account requires BDUSS and stoken.

Adding an common account requires all cookies of account.

Administrators can control user network traffic.



#### register

Registration can only be registered as a regular user.

Administrator users require other administrators to set them up, or modify ordinary users to administrators in the database. (Need to modify the role permissions of the account)

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
npm run test:unit
```
