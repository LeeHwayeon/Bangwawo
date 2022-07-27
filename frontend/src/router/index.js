import { createWebHistory, createRouter } from "vue-router";

// const onlyAuthUser = async (to, from, next) => {
//   const checkUserInfo = store.getters["userStore/checkUserInfo"];
//   const getUserInfo = store._actions["userStore/getUserInfo"];
//   let token = sessionStorage.getItem("access-token");
//   if (checkUserInfo == null && token) {
//     await getUserInfo(token);
//   }
//   if (checkUserInfo === null) {
//     alert("로그인이 필요한 페이지입니다..");
//     next("/user/login");
//   } else {
//     next();
//   }
// };

// const onlyAdmin = async (to, from, next) => {
//   // console.log(store);
//   const checkUserInfo = store.getters["userStore/checkUserInfo"];
//   const getUserInfo = store._actions["userStore/getUserInfo"];
//   let token = sessionStorage.getItem("access-token");
//   if (checkUserInfo == null && token) {
//     await getUserInfo(token);
//   }
//   if (checkUserInfo.role !== 1) {
//     alert("관리자만 입장 가능한 페이지입니다..");
//     next("/");
//   } else {
//     next();
//   }
// };

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("@/views/HomeView"),
  },
  {
    path: "/secret",
    name: "secret",
    component: () => import("@/views/SecretFriendView"),
  },
  {
    path: "/classlist",
    name: "classlist",
    component: () => import("@/views/ClassListView"),
  },
  {
    path: "/classdetail",
    name: "classdetail",
    component: () => import("@/views/ClassDetailView"),
  },
  {
    path: "/secrettalk",
    name: "secrettalk",
    component: () => import("@/views/SecretTalkView"),
  },
  {
    path: "/user",
    name: "user",
    component: () => import("@/views/UserView"),
    children: [
      {
        path: "login",
        name: "login",
        component: () => import("@/views/LoginView"),
      },
      {
        path: "signup/student",
        name: "signupStudent",
        component: () => import("@/views/SignupStudentView"),
      },
      {
        path: "signup/volunteer",
        name: "signupVolunteer",
        component: () => import("@/views/SignupVolunteerView"),
      },
    ],
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});