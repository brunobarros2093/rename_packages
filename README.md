﻿# rename_packages(args)

## Em Desenvolvimento [WIP] 

Rename Packages does what looks like, it rename packages in a Java projects. Probably works with other languages, but I didnt test it. 


Renomeia o nome do pacote em várias classes Java encontradas no diretório especificado.

    Args:
        path (str): O caminho para o diretório contendo as classes Java.
        old_packages (list): Uma lista de nomes de pacotes antigos a serem substituídos.
        new_package_name (str): O novo nome do pacote.
        package_description (str, opcional): Uma descrição para adicionar acima da declaração de pacote nas classes Java. Padrão é None.

    Returns:
        None
