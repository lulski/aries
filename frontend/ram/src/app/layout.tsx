import "@mantine/core/styles.css";
import AppShellLayout from "@/app/components/AppShellLayout";
("@/components/Layout");
import "./globals.css";

import React from "react";
import {
  createTheme,
  ColorSchemeScript,
  MantineProvider,
  mantineHtmlProps,
  Container,
  Box,
  Button,
  Center,
  Group,
  Image,
  Stack,
  Flex,
  AppShell,
  Burger,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import Layout from "@/app/components/AppShellLayout";

const theme = createTheme({
  /** Your theme override here */
  //fontFamily: "Open Sans, sans-serif",
  //  primaryColor: "cyan",
});

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" {...mantineHtmlProps}>
      <head>
        <style>
          {`
            body {
              background-color: #e5e5f7;
              opacity: 0.8;
              background-size: 10px 10px;
              background-image: repeating-linear-gradient(45deg,hsl(237, 48.80%, 91.60%) 0,rgb(220, 221, 241) 1px, #e5e5f7 0, #e5e5f7 50%);
            }
          `}
        </style>
        <ColorSchemeScript />
      </head>
      <meta
        name="viewport"
        content="minimum-scale=1, initial-scale=1, width=device-width, user-scalable=no"
      />
      <body>
        <MantineProvider theme={theme}>
          {/* <Group>
            <Flex>
              <Button variant="default" component="a" href="/posts">
                Random rambling
              </Button>
              <Button variant="default" component="a" href="/about">
                About
              </Button>
              <Button variant="default" component="a" href="/guestbook">
                Guestbook
              </Button>
            </Flex>

            <Container>{children}</Container>
          </Group> */}
          <AppShellLayout>{children}</AppShellLayout>
        </MantineProvider>
      </body>
    </html>
  );
}
