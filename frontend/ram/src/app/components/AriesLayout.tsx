"use client";

import { Anchor, AppShell, Burger, Group, Title } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";
import { ThemeToggleButton } from "./ThemeToggleButton/ThemeToggleButton";

export default function AriesLayout({ children }: { children: ReactNode }) {
  const [navbarIsOpen, { toggle }] = useDisclosure();

  const layoutProps: AriesLayoutProps = {
    navbarItems: [
      { label: "Home", href: "/" },
      { label: "Posts", href: "/posts" },
      { label: "About", href: "/about" },
    ],
  };

  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{
        width: 300,
        breakpoint: "sm",
        collapsed: { mobile: !navbarIsOpen },
      }}
      padding="md"
    >
      <AppShell.Header>
        <Group h="100%" px="md" justify="space-between">
          {
            <Burger
              opened={navbarIsOpen}
              onClick={toggle}
              hiddenFrom="sm"
              size="sm"
            />
          }

          <Title>Coco Classico</Title>

          <ThemeToggleButton />

          <Anchor size="md" href="/login">
            Login
          </Anchor>
        </Group>
      </AppShell.Header>

      <AppShell.Navbar p="md">
        {layoutProps.navbarItems?.map((item, index) => (
          <Anchor size="md" key={index} href={item.href}>
            {item.label}
          </Anchor>
        ))}
      </AppShell.Navbar>

      <AppShell.Main>{children}</AppShell.Main>
    </AppShell>
  );
}
