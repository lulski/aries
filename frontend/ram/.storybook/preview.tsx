import { MantineProvider } from "@mantine/react";
import type { Preview } from "@storybook/nextjs-vite";
import "../src/app/globals.css";
import { AriesLayout } from "../src/components/AriesLayout";
import { ThemeProvider } from "../src/components/ThemeProvider";

const preview: Preview = {
  decorators: [
    (Story) => (
      <MantineProvider defaultColorScheme="auto">
        <ThemeProvider>
          <AriesLayout>
            <Story />
          </AriesLayout>
        </ThemeProvider>
      </MantineProvider>
    ),
  ],
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },

    a11y: {
      // 'todo' - show a11y violations in the test UI only
      // 'error' - fail CI on a11y violations
      // 'off' - skip a11y checks entirely
      test: "todo",
    },
  },
};

export default preview;
