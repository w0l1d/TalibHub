interface NavbarLink {
    route: string;
    icon: string;
    label: string;
    highlight?: boolean;
}

const NavbarData: NavbarLink[] = [
    { route: '/home', icon: 'fa-solid fa-house', label: 'Home' , highlight: false},
    { route: '/studentProfile', icon: 'fa-solid fa-user', label: 'Profile', highlight: true },
    { route: '/news', icon: 'fa-solid fa-newspaper', label: 'News', highlight: false },
    { route: '/logout', icon: 'fa-solid fa-right-from-bracket', label: 'Logout', highlight: false },
];

export default NavbarData;
