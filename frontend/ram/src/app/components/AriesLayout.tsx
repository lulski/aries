"use client";

import { AppShell, Burger, Container, Text } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import Link from "next/link";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";

export default function AriesLayout({
  children,
  navbarItems,
}: AriesLayoutProps & { children: ReactNode }) {
  const [opened, { toggle }] = useDisclosure();

  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{
        width: 300,
        breakpoint: "sm",
        collapsed: { mobile: !opened },
      }}
      padding="md"
    >
      <AppShell.Header>
        <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
        <Container ta={"right"}>
          <div>Logo</div>
        </Container>
      </AppShell.Header>

      <AppShell.Navbar p="md">
        {navbarItems.map((item, index) => (
          <Text size="md" key={index}>
            <Link href={item.href}>{item.label}</Link>
          </Text>
        ))}
      </AppShell.Navbar>

      <AppShell.Main>{children}</AppShell.Main>
    </AppShell>
  );
}
