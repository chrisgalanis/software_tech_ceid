## Setup Local Server 

### VSCode
For noobs that use VSCode you can use: Live Server Plugin from VsCode

### NVim
Now for the pros that are using NVim. The setup is: 
1) Install: npm
2) Install: npm install -g live-server
3) Make this changes: (this are for LazyVim)

  Open Neovim and create the file:
      vim
      :edit ~/.config/nvim/lua/plugins/live-server.lua

  Paste the following code into live-server.lua. This is tailored for LazyVim and includes the setup you provided:
      lua
      return {
        {
          "barrett-ruth/live-server.nvim",
          build = "npm install -g live-server", -- Corrected from 'npm add' to 'npm install'
          cmd = { "LiveServerStart", "LiveServerStop" },
          config = function()
            require("live-server").setup({
              -- Optional: Customize settings here
              -- port = 5555, -- Default port, change if needed
              -- browser = "firefox", -- Specify browser if desired
            })
          end,
        },
      }

  Save the file (:w).
  Run :Lazy in Neovim to open the Lazy UI.
  Press S to sync, which will install live-server.nvim and execute the build command to install live-server globally via npm (assuming Node.js is installed).

4) Create Keybindings

Open or create the keymaps file:
    vim
    :edit ~/.config/nvim/lua/config/keymaps.lua
Add the following code (or append if the file already exists):
    lua
    -- Live Server keybindings
    vim.keymap.set("n", "<leader>ls", "<cmd>LiveServerStart<CR>", { desc = "Start Live Server" })
    vim.keymap.set("n", "<leader>lk", "<cmd>LiveServerStop<CR>", { desc = "Stop Live Server" })
