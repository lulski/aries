import "@mantine/core/styles.css";
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
} from "@mantine/core";

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
          <Center>
            <Stack className="parent">
              <Group h="100">
                <Button variant="default" component="a" href="/posts">
                  Random rambling
                </Button>
                <Button variant="default" component="a" href="/about">
                  About
                </Button>
                <Button variant="default" component="a" href="/guestbook">
                  Guestbook
                </Button>
              </Group>
              <Container>{children}</Container>
            </Stack>
          </Center>
        </MantineProvider>
      </body>
    </html>
  );
}
